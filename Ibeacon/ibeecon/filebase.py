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
# 5分単位にし、現在の時刻から5分刻みにする
def time5():
    dt_now = datetime.datetime.now()
    dt_now = dt_now.replace(minute=dt_now.minute - dt_now.minute % 5, second=0, microsecond=0)
    # Dateの削除
    dt_now = dt_now.replace(hour=dt_now.hour, minute=dt_now.minute, second=0, microsecond=0)
    # HH-MMの形式にする
    dt_now = dt_now.strftime(u'%H-%M')
    return dt_now


def main():
    # ===================== Firebase =====================================
    # このPythonファイルと同じ階層に認証ファイルを配置して、ファイル名を格納
    JSON_PATH = './geekcamp.json'

    # Firebase初期化
    cred = credentials.Certificate(JSON_PATH)
    firebase_admin.initialize_app(cred)
    db = firestore.client()
    #name = np.array(['hoge','fuga','piyo'],dtype=str)

    # ====================================================================

    # 3000秒ごとに実行
    while True:
        # CSVファイルを読み込む
        major = []
        uuid = []
        with open('./beacon_out.csv', 'r') as csv_file:
            csv_reader = reader(csv_file)
            for row in csv_reader:
                major.append(row[0])
                uuid.append(row[1])
        
        # 時刻を取得
        dt_now = datetime.datetime.now()
        
        # Userコレクションに登録されているユーザーを取得
        doc_user = db.collection('User').get()

        allUsersUUIDList = []
        allUsersNAMEList = []
        # ユーザーを一人ずつ取り出して、UUID比較
        for _ in doc_user:
            # ユーザ１人のデータを取得
            doc = db.collection(u'User').document(_.id)
            my_dict = doc.get().to_dict()
            # print("mydict")
            # print(my_dict)
            # print("end")     
            # print(time5())
            # UUIDが一致するものを探す
            for i in uuid:
                if my_dict['UUID'] in i:
                    # もしUUIDが一致したら、そのユーザのUUIDとNAMEをリスト追加する
                    # 既にある場合は、リストに追加しない
                    if my_dict['UUID'] not in allUsersUUIDList:
                        allUsersUUIDList.append(my_dict['UUID'])
                        allUsersNAMEList.append(my_dict['NAME'])
            try:
                    # Firestoreのコレクションにアクセス
                
                doc_ref = db.collection(u'RoomLog').document(dt_now.strftime(u'%Y-%m-%d')).collection(u"Times").document(time5())
                    #Firestoreにドキュメントidを指定しないで１つづつニュースを保存
                data = {
                    u'NAME': allUsersNAMEList,
                    u'UUID': allUsersUUIDList,
                }
                doc_ref.set(data)
                # print('ok')
            except:
                print('error')
        schedule(2, worker)

if __name__ == '__main__':
    main()
