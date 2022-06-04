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
    #print(time.time())
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
            #print(csv_reader)
            major = []
            uuid = []
            for row in csv_reader:
                major.append(row[0])
                uuid.append(row[1])

        dt_now = datetime.datetime.now()

        for _ in doc_user:
            #print(_.id)
            doc = db.collection(u'User').document(_.id)
            my_dict = doc.get().to_dict()
            for i in uuid:
                # print(i)
                if my_dict['UUID'] in i:
                    try:
                    # Firestoreのコレクションにアクセス
                        doc_ref = db.collection(u'RoomLog').document(dt_now.strftime(u'%Y-%m-%d')).collection(u"Times").document(dt_now.strftime(u'%H:%M'))
                    #Firestoreにドキュメントidを指定しないで１つづつニュースを保存
                        doc_ref.set({
                            u'NAME': my_dict['NAME'],
                            u'TIME':dt_now.strftime(u'%H:%M'),
                            u'UUID':my_dict['UUID'],
                        })
                        print('ok')
                    except:
                        print('error')
        schedule(300, worker)
print('done')


if __name__ == '__main__':
    main()