import requests
from bs4 import BeautifulSoup
import re
import datetime
from firebase_admin import firestore
import firebase_admin
from firebase_admin import credentials


def main():
    # ===================== Firebase =====================================
    # このPythonファイルと同じ階層に認証ファイルを配置して、ファイル名を格納
    JSON_PATH = './geekcamp.json'

    # Firebase初期化
    cred = credentials.Certificate(JSON_PATH)
    firebase_admin.initialize_app(cred)
    db = firestore.client()
    # ====================================================================

    dt_now = datetime.datetime.now()
    url = 'https://news.yahoo.co.jp/categories/domestic'
    r = requests.get(url)
    soup = BeautifulSoup(r.content, 'html.parser')
    res = soup.find_all(href=re.compile('news.yahoo.co.jp/pickup'))

    for news in res:
        try:
            # Firestoreのコレクションにアクセス
            doc_ref = db.collection('news')
            # Firestoreにドキュメントidを指定しないで１つづつニュースを保存
            doc_ref.add({
                'title': news.text,
                'url': news.attrs['href'],
                'date': dt_now.strftime('%Y年%m月%d日'),
            })
        except:
            print('error')

    print('done')


if __name__ == '__main__':
    main()