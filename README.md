
# 人の入り見れる蔵(hito no iri mi reru zou)

![](https://i.imgur.com/UxpA2Gz.png)

## Introduction

このシステムは、スマートフォンから送られてくるビーコン情報をRaspberry Piで受信し、部屋の入退室管理を行うものです。
また、現在誰が部屋にいるのか、Web上で確認することができます。

## Environment

| Tool    | Version  |
| ------- | -------- |
| Python  | 3.10.5   |
| Kotlin        |  1.6.21        |
| Node.js | 16.15.0 |

## Usage

### requirements

``` pip install -r requirements.txt ```

### raspberrypi

#### send_beacon

``` cd Ibeacon/bluez-beacon ```
```sudo ./ibeacon 200 ba4d8ef751a3110ca55dbca1afbba494 56562 1 -29```

#### receiving_beacon

``` cd Backend/beacon_send ```
``` python3 beacon.py ```

### Backend

```cd Backend```
``` python3 firebase.py ```

## YouTube

https://youtu.be/aXrKhL0umm0?si=0yWlDZsZpFt5BRA2

## Blueprint

![](https://i.imgur.com/ivNt2UX.png)

## Slide

https://docs.google.com/presentation/d/1HPZSVCowMsqZpYC6BxLSLmziRzbQEojV/edit?usp=sharing&ouid=103737357734306374267&rtpof=true&sd=true

## Contributing

プルリクエストは歓迎します。大きな変更については、まず課題を作成して、変更したい内容を議論してください。

Pull requests are welcome. For major changes, please open an issue first to discuss what you would like to change.

## License

[![SUSHI-WARE LICENSE](https://img.shields.io/badge/license-SUSHI--WARE%F0%9F%8D%A3-blue.svg)](https://github.com/MakeNowJust/sushi-ware)
