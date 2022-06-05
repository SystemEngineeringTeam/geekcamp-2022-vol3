import type { NextApiRequest, NextApiResponse } from 'next';
import { getFirestore } from 'firebase-admin/firestore';
const admin = require('firebase-admin');


function getDate() {
    const dt = new Date();
    var y = dt.getFullYear();
    var m = ('00' + (dt.getMonth() + 1)).slice(-2);
    var d = ('00' + dt.getDate()).slice(-2);
    return (y + '-' + m + '-' + d);
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

// 渡されたUUIDから、USERのUUIDを検索し、見つかった場合は、UserのICONIMAGEを返す
async function getUserIconImage(uuid: string) {
    const db = getFirestore();
    const user = await db.collection('User').doc().get();
    console.log(user.data());
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
        console.log(getDate());
        const entering_room_status = await RoomLog.doc(getDate()).collection("Times").doc(getTime()).get();
        const current_uuid = entering_room_status.data()?.UUID;
        const current_name = entering_room_status.data()?.NAME;

        console.log("current_uuid:" + current_uuid);
        console.log("current_name:" + current_name);
        // ユーザーのアイコン画像を取得する
        // 型がObjectである為、Object.keysでforEachで回す
        // ['0eb072af-55ee-70cd-8026-d15875eb7a5f','d0d3e56a-d4f7-4c31-af6f-d367bf93630d']

        let icon_image_urls: string[] = [];

        Object.keys(current_uuid).forEach(async (key) => {
            // 単一のUUIDを取得する
            const uuid = current_uuid[key];
            // Userに入ってる全てのUUIDを取得する
            const all_user = await db.collection('User').get();

            // 取得したUUIDと一致するものがあれば、アイコン画像を取得する
            all_user.forEach(doc => {
                if (doc.data().UUID === uuid) {
                    icon_image_urls.push(doc.data().ICONIMAGE);
                    console.log(doc.data().ICONIMAGE);
                }
            });
        })

        // レスポンスを返す
        setTimeout(() => {
            res.status(200).json({
                current_name,
                icon_image_urls,
            });
        }
            , 1000);
    }
}
