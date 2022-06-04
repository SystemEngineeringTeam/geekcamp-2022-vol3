import datetime
from firebase_admin import firestore
import firebase_admin
from firebase_admin import credentials
import time
import threading
import array
import numpy as np

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

def main():
    # ===================== Firebase =====================================
    # このPythonファイルと同じ階層に認証ファイルを配置して、ファイル名を格納
    JSON_PATH = './geekcamp.json'

    # Firebase初期化
    cred = credentials.Certificate(JSON_PATH)
    firebase_admin.initialize_app(cred)
    db = firestore.client()
    name = ['hoge','fuga','piyo']
    # ====================================================================


    while True:
        dt_now = datetime.datetime.now()
        try:
            # Firestoreのコレクションにアクセス
            doc_ref = db.collection(u'RoomLog').document(dt_now.strftime(u'%Y-%m-%d')).collection(u"Times").document(dt_now.strftime(u'%H:%M'))
            # Firestoreにドキュメントidを指定しないで１つづつニュースを保存
            doc_ref.set({
                u'NAME': name[np.random.randint(0,3)],
                u'TIME':dt_now.strftime(u'%H:%M'),
            })
        except:
            print('error')
        print(dt_now.strftime(u'%Y-%m-%d %H:%M:%S'))
        schedule(1, worker)

print('done')


if __name__ == '__main__':
    main()

# import requests
# from bs4 import BeautifulSoup
# import re
# import datetime
# import time
# from firebase_admin import firestore
# import firebase_admin
# from firebase_admin import credentials
# # from beacon_send import beacon

# roomlog_name = []
# dt_now = datetime.datetime.now()
# tmp_beacon_major = ["566656", "56562","2","56562"]
# tmp_beacon_uuid = ["ba4d8ef7-51a3-110c-a55d-bca1afbba494","ba4d8ef7-51a3-110c-a55d-bca1afbba495"]
# tmp2_beacon_uuid = ["ba4d8ef7-51a3-110c-a55d-bca1afbba494","ba4d8ef7-51a3-110c-a55d-bca1afbba496"]
# tmp_beacon_name = ["mikan","tako","makino"]
# def main():
#     # ===================== Firebase =====================================
#     # このPythonファイルと同じ階層に認証ファイルを配置して、ファイル名を格納
#     JSON_PATH = './geekcamp.json'

#     # Firebase初期化
#     cred = credentials.Certificate(JSON_PATH)
#     firebase_admin.initialize_app(cred)
#     db = firestore.client()
#     docs_user = db.collection('User').get()
#     # ====================================================================

#     # ====================== Beacon ======================================
#     while True:
#         # if beacon.packet.major == '56562':
#         for _ in tmp_beacon_major:
#             if _ == '56562':
#                 #for doc in docs_user:
#                 #if beacon.packet.uuid == doc.UUID:
#                 if tmp_beacon_uuid in tmp2_beacon_uuid:
#                     # roomlog_name.append(doc.NAME)
#                     roomlog_name.append(tmp_beacon_name[_])
#             # Firestoreのコレクションにアクセス
#                 try:
#                     doc_ref = db.collection('TempLog')
#                 # Firesoreにデータを追加
#                     doc_ref.add({
#                         'NAME': roomlog_name,
#                         'UUID': tmp_beacon_uuid,
#                         'TIMES': dt_now,
#                     })
#                 except:
#                     print('error')
#         # for news in res:
#         #     try:
#         #         # Firestoreのコレクションにアクセス
#         #         doc_ref = db.collection('')
#         #         # Firestoreにドキュメントidを指定しないで１つづつニュースを保存
#         #         doc_ref.add({
#         #             'title': news.text,
#         #             'url': news.attrs['href'],
#         #             'date': dt_now.strftime('%Y年%m月%d日'),
#         #         })
#         #     except:
#         #         print('error')
#         time.sleep(10)
#         print('done')
#     # ====================================================================

# if __name__ == '__main__':
#     main()