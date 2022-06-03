import type { NextApiRequest, NextApiResponse } from 'next';
import { cert } from 'firebase-admin/app';
import { getFirestore } from 'firebase-admin/firestore';

const serviceAccount = require('../../geekcamp-ed.json');
const admin = require('firebase-admin');


// 日付(YYYY/MM/DD)を取得し、パースする(YYYY-MM-DD)
function getDate() {
    const date = new Date().toLocaleDateString();
    const date_parse = date.split('/').join('-');
    return date_parse;
}
export default async function handler(
    req: NextApiRequest,
    res: NextApiResponse,
) {
    const COLLECTION_NAME = 'RoomLog';
    const db = getFirestore();
    let users: string[] = [];


    // 初期化を行う
    if (admin.apps.length === 0) {
        admin.initializeApp({
            credential: cert(serviceAccount),
        });
    }

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
