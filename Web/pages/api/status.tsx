import type { NextApiRequest, NextApiResponse } from 'next';
import { getFirestore } from 'firebase-admin/firestore';
const admin = require('firebase-admin');


// 日付(YYYY/MM/DD)を取得し、パースする関数(YYYY-MM-DD)
function getDate() {
    const date = new Date().toLocaleDateString();
    const date_parse = date.split('/').join('-');
    return date_parse;
}


const cert = {
    projectId: process.env.PROJECT_ID,
    clientEmail: process.env.CLIENT_EMAIL,
    privateKey: process.env.PRIVATE_KEY?.replace(/\\n/g, "\n"),
};

// ステータスを取得する関数
export default async function handler(
    req: NextApiRequest,
    res: NextApiResponse,
) {
    // コレクション指定
    const COLLECTION_NAME = 'RoomLog';


    // 初期化をする場合、最初の１回だけでいい
    // lengthを取ると、初期化されているかどうかを確認できる
    // https://github.com/firebase/firebase-tools/issues/1532
    if (admin.apps.length === 0) {
        admin.initializeApp({
            credential: admin.credential.cert(cert),
        });
        console.log(process.env.PROJECT_ID);
    }
    const db = getFirestore();

    
    // レスポンス処理
    // 現在の入室状況を取得する
    if (req.method === 'GET') {
        const doc = await db.collection(COLLECTION_NAME).get();
        console.log(doc);
        doc.forEach((i) => {
            if (i.id === getDate()) {
                console.log(i.id, '=>', i.data());
                res.status(200).json(i.data());
            }
        });

    }
}
