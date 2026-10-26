"""가상 로봇 시뮬레이터.

서로 다른 두 제조사(ALPHA, BRAVO) 형식으로 상태를 MQTT 에 발행하고,
robots/+/cmd 토픽으로 오는 명령을 받아 화면에 찍는다.

실행:  python robot_sim.py --broker localhost --interval 2
중지:  Ctrl+C
"""
import argparse
import json
import random
import signal
import sys
import time
from datetime import datetime, timezone

import paho.mqtt.client as mqtt

ALPHA_STATES = ["IDL", "MOV", "WRK", "CHG", "ERR"]
BRAVO_STATES = ["idle", "moving", "working", "charging", "error"]


class Robot:
    def __init__(self, robot_id, vendor):
        self.robot_id = robot_id
        self.vendor = vendor
        self.battery = random.uniform(40, 100)
        self.x = random.uniform(0, 20)
        self.y = random.uniform(0, 20)
        self.state_index = 0

    def step(self):
        self.battery = max(0.0, self.battery - random.uniform(0.1, 0.8))
        self.x += random.uniform(-0.5, 0.5)
        self.y += random.uniform(-0.5, 0.5)
        if random.random() < 0.3:
            self.state_index = random.randrange(5)

    def payload(self):
        if self.vendor == "ALPHA":
            return {
                "id": self.robot_id,
                "st": ALPHA_STATES[self.state_index],
                "bat": round(self.battery, 1),
                "pos": {"x": round(self.x, 2), "y": round(self.y, 2)},
                "ts": int(time.time()),
            }
        return {
            "robotId": self.robot_id,
            "status": BRAVO_STATES[self.state_index],
            "batteryLevel": round(self.battery / 100.0, 3),
            "x": round(self.x, 2),
            "y": round(self.y, 2),
            "timestamp": datetime.now(timezone.utc).strftime("%Y-%m-%dT%H:%M:%SZ"),
        }


def on_connect(client, userdata, flags, reason_code, properties):
    print(f"[sim] connected: {reason_code}")
    client.subscribe("robots/+/cmd", qos=1)


def on_message(client, userdata, msg):
    print(f"[sim] command received topic={msg.topic} payload={msg.payload.decode('utf-8', 'replace')}")


def main():
    parser = argparse.ArgumentParser()
    parser.add_argument("--broker", default="localhost")
    parser.add_argument("--port", type=int, default=1883)
    parser.add_argument("--interval", type=float, default=2.0, help="발행 주기(초)")
    parser.add_argument("--robots", default="amr-01:ALPHA,amr-02:ALPHA,arm-01:BRAVO",
                        help="id:VENDOR 를 콤마로 나열")
    args = parser.parse_args()

    robots = []
    for item in args.robots.split(","):
        robot_id, vendor = item.split(":")
        robots.append(Robot(robot_id.strip(), vendor.strip().upper()))

    client = mqtt.Client(mqtt.CallbackAPIVersion.VERSION2, client_id="robot-sim")
    client.on_connect = on_connect
    client.on_message = on_message
    client.connect(args.broker, args.port, keepalive=30)
    client.loop_start()

    running = {"on": True}

    def stop(*_):
        running["on"] = False

    signal.signal(signal.SIGINT, stop)
    signal.signal(signal.SIGTERM, stop)

    print(f"[sim] publishing {len(robots)} robots every {args.interval}s. Ctrl+C to stop.")
    while running["on"]:
        for robot in robots:
            robot.step()
            topic = f"robots/{robot.robot_id}/status"
            body = json.dumps(robot.payload())
            client.publish(topic, body, qos=1)
            print(f"[sim] -> {topic} {body}")
        time.sleep(args.interval)

    client.loop_stop()
    client.disconnect()
    print("[sim] stopped")
    return 0


if __name__ == "__main__":
    sys.exit(main())
