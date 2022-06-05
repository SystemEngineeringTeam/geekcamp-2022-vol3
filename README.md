# Geekcamp 2022 vol3

## Introduction


## Environment

| Tool    | Version  |
| ------- | -------- |
| Node.js | v16.15.0 |

## Usage

### requirements

``` pip install -r requirements.txt ```

### raspberrypi
#### send_beacon
``` cd Ibeacon/bluez-beacon ```
``` sudo ./ibeacon 200 ba4d8ef751a3110ca55dbca1afbba494 56562 1 -29```

#### receiving_beacon
``` cd Backend/beacon_send ```
``` python3 beacon.py ```

### Backend

``` cd Backend```
``` python3 firebase.py ```

## Contributing

プルリクエストは歓迎します。大きな変更については、まず課題を作成して、変更したい内容を議論してください。

Pull requests are welcome. For major changes, please open an issue first to discuss what you would like to change.

## License

このリポジトリはMIT SUSHI-WAREライセンスの条件の下でオープンソースとして利用出来ます。

This repository is available as open source under the terms of the MIT SUSHI-WARE license.

![LICENSE: MIT SUSHI-WARE🍣](https://raw.githubusercontent.com/watasuke102/mit-sushi-ware/master/MIT-SUSHI-WARE.svg)
