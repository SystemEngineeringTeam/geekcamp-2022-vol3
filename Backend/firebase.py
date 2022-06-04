import requests
from bs4 import BeautifulSoup
import re
import datetime
from firebase_admin import firestore
import firebase_admin
from firebase_admin import credentials
from beacon_send import beacon

roomlog_name = []
dt_now = datetime.datetime.now()
def main():
    # ===================== Firebase =====================================
    # このPythonファイルと同じ階層に認証ファイルを配置して、ファイル名を格納
    JSON_PATH = './geekcamp.json'

    # Firebase初期化
    cred = credentials.Certificate(JSON_PATH)
    firebase_admin.initialize_app(cred)
    db = firestore.client()
    docs_user = db.collection('User').get()
    # ====================================================================

    # ====================== Beacon ======================================
    while True:
        if beacon.packet.major == '56562':
            for doc in docs_user:
                if beacon.packet.uuid == doc.UUID:
                    roomlog_name.append(doc.NAME)
            # Firestoreのコレクションにアクセス
                try:
                    doc_ref = db.collection('TempLog')
                # Firesoreにデータを追加
                    doc_ref.add({
                        'NAME': roomlog_name,
                        'UUID': beacon.packet.uuid,
                    })
                except:
                    print('error')
        # for news in res:
        #     try:
        #         # Firestoreのコレクションにアクセス
        #         doc_ref = db.collection('')
        #         # Firestoreにドキュメントidを指定しないで１つづつニュースを保存
        #         doc_ref.add({
        #             'title': news.text,
        #             'url': news.attrs['href'],
        #             'date': dt_now.strftime('%Y年%m月%d日'),
        #         })
        #     except:
        #         print('error')

        print('done')
    # ====================================================================

if __name__ == '__main__':
    main()