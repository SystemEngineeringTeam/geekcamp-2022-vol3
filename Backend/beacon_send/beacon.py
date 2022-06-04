import time
import threading
import csv
from beacontools import BeaconScanner, IBeaconFilter, IBeaconAdvertisement

major_list = []
uuid_list = []

def worker():
    print(time.time())
    time.sleep(8)

def schedule(interval, f, wait=True):
    base_time = time.time()
    next_time = 0
    t = threading.Thread(target=f)
    t.start()
    if wait:
        t.join()
    next_time = ((base_time - time.time()) % interval) or interval
    time.sleep(next_time)

while True:
    def callback(bt_addr, rssi, packet, additional_info):
        # major_list = []
        # uuid_list = []
        major_list.append(packet.major)
        uuid_list.append(packet.uuid)
        #f = open('beacon_out.csv', 'w')
        # data = [packet.major,packet.uuid]
        # writer = csv.writer(f)
        # writer.writerow(data)
        # f.close()
        #print("<%s, %d> %s %s" % (bt_addr, rssi, packet, additional_info))

# scan for all iBeacon advertisements from beacons with certain properties:
# - uuid
# - major
# - minor
# at least one must be specified.

    scanner = BeaconScanner(callback, device_filter=IBeaconFilter(uuid="e5b9e3a6-27e2-4c36-a257-7698da5fc140")
    )
    scanner.start()
    time.sleep(5)
    scanner.stop()

# scan for all iBeacon advertisements regardless from which beacon
    scanner = BeaconScanner(callback,
        packet_filter=IBeaconAdvertisement
    )
    scanner.start()
    time.sleep(5)
    scanner.stop()
    print(major_list,uuid_list)
    f = open('beacon_out.csv', 'w')
    data = [major_list,uuid_list]
    writer = csv.writer(f,lineterminator='\n')
    writer.writerow(data)
    f.close()
    schedule(5, worker)
