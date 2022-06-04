import datetime
from firebase_admin import firestore
import firebase_admin
from firebase_admin import credentials
import time
import threading
from csv import reader

uuid = []
major = []

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
    #name = np.array(['hoge','fuga','piyo'],dtype=str)

    doc_user = db.collection('User').get()
    # ====================================================================


    while True:
        with open('./beacon_out.csv', 'r') as csv_file:
            csv_reader = reader(csv_file)
    # Passing the cav_reader object to list() to get a list of lists
            for row in csv_reader:
                major.append(str(row[0]))
                uuid.append(str(row[1]))
            #print(list_of_rows[0][0])
            print(major)
            #print(list_of_rows[0][1])
        dt_now = datetime.datetime.now()
        try:
            # Firestoreのコレクションにアクセス
            doc_ref = db.collection(u'RoomLog').document(dt_now.strftime(u'%Y-%m-%d')).collection(u"Times").document(dt_now.strftime(u'%H:%M'))
            # Firestoreにドキュメントidを指定しないで１つづつニュースを保存
            doc_ref.set({
                u'NAME': 'hoge',
                u'TIME':dt_now.strftime(u'%H:%M'),
            })
        except:
            print('error')
        #print(dt_now.strftime(u'%Y-%m-%d %H:%M:%S'))
        for _ in doc_user:
            #print(_.id)
            doc = db.collection(u'User').document(_.id)
            my_dict = doc.get().to_dict()
            # print(my_dict["UUID"])
            # print(my_dict["NAME"])
        schedule(5, worker)
print('done')


if __name__ == '__main__':
    main()

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