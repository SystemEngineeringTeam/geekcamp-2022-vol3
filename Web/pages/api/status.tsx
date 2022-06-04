import type { NextApiRequest, NextApiResponse } from 'next';
import { getFirestore } from 'firebase-admin/firestore';
const admin = require('firebase-admin');


// 日付(YYYY/MM/DD)を取得し、パースする関数(YYYY-MM-DD)
function getDate() {
    const date = new Date().toLocaleDateString();
    const date_parse = date.split('/').join('-');
    return date_parse;
}

// 時間(HH-MM)を取得
function getTime() {

    const getHour = new Date().getHours();
    const getMinutes = new Date().getMinutes();

    // getMinutesを5分単位にして、５分前にする
    const getMinutes_5 = Math.floor(getMinutes / 5) * 5;

    // getMinutes_5が0の場合は、00にする
    if (getMinutes_5 === 0) {
        const getMinutes_5_str = '00';
        return getHour + '-' + getMinutes_5_str;
    } else {
        if (getMinutes_5 < 10) {
            const getMinutes_5_str = '0' + getMinutes_5;
            return getHour + '-' + getMinutes_5_str;
        } else {
            return getHour + '-' + getMinutes_5;
        }
    }
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
        const RoomLog = db.collection(COLLECTION_NAME);
        const nowDate = await RoomLog.doc(getDate()).collection("Times").doc(getTime()).get();
        console.log(getTime());
        console.log(nowDate.data());
        res.status(200).json(nowDate.data());
    }
}
