# FOX Server Simulator

FOX のサーバをモックするプロジェクトです。現在は Deferred Deep Link の API を提供しています。 Play Framework 2.5 を使ってます。

API -
1. /deeplink1?appId=xxxx&xuniq=xxx&expiry=x  :   登録されているアプリの Deep Link をJSONで返す
2. /deeplink2?appId=xxxx&xuniq=xxx&expiry=x  :   1時間内に5の倍数(変えれる)番目のリクエストの時だけ Deep Link をJSONで返す、他の場合は空レスポンスを返す
3. /deeplink3?appId=xxxx&xuniq=xxx&expiry=x  :   空レスポンス
4. /reset                                    :   リクエストカウンタをリセットする

appId, xuniq, expiry のパラメータが必要。渡さないと空レスポンス。
レコードは csv ファイルで管理する。