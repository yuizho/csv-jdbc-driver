# CSV JDBC Driver

CSVファイルをデータベーステーブルのように扱い、SQLを使用してデータを参照できる簡易JDBCドライバです。
JDBCのお勉強ように作成したおもちゃプロジェクトです。

## 使い方

JDBCドライバとして `dev.yuizho.jdbc.CsvDriver` が登録されています（Java SPIの仕組みを利用）。
以下の形式のJDBC URLを使用して接続を取得します。

**注意: このドライバは、1つのコネクションにつき1つのCSVファイルを対象とします。**

### Classpath上のCSVを参照

```java
// クラスパスルートにある demo.csv を読み込む場合
String url = "jdbc:classpath://demo.csv";
try (Connection conn = DriverManager.getConnection(url)) {
    // ...
}
```

### ファイルシステム上のCSVを参照

```java
// 絶対パスなどで指定
String url = "jdbc:file:///path/to/your/data.csv";
try (Connection conn = DriverManager.getConnection(url)) {
    // ...
}
```

### 使用例

```java
try (Connection conn = DriverManager.getConnection("jdbc:classpath://demo.csv");
     Statement stmt = conn.createStatement();
     ResultSet rs = stmt.executeQuery("SELECT * FROM demo WHERE id = '1'")) {

    while (rs.next()) {
        System.out.println(rs.getString("name"));
    }
}
```

## アーキテクチャ概要

本ドライバは、以下のライブラリを利用して実装されています。

*   **SQL解析**: [JSQLParser](https://github.com/JSQLParser/JSqlParser) を使用して `SELECT` 文を解析し、条件抽出を行います。
*   **CSV解析**: [Apache Commons CSV](https://commons.apache.org/proper/commons-csv/) を使用してCSVファイルの読み込みとレコード解析を行います。

### コンポーネント

*   **CsvDriver**: JDBCドライバのエントリーポイント。URLスキーム（`jdbc:classpath://`, `jdbc:file://`）に応じて適切なConnectionを生成します。
*   **CsvConnection**: 接続情報（CSVファイルのパスなど）を保持します。
    *   **実装のポイント**: 一般的なRDBMS（MySQLなど）向けのドライバの `Connection` はサーバーとの通信Socketを維持し続けますが、本ドライバが扱うファイルストリームは一度使うと再利用できません。そのため、`Connection` でストリームを保持し続けるのではなく、クエリ実行のたびに都度ファイルを開くようになっています。
*   **CsvStatement**: SQLを受け取り、CSVレコードをストリーム処理でフィルタリングして `ResultSet` を返します。データはメモリに全展開せず、ストリーム処理されます（ただし、実装の都合上、一部の操作でメモリに展開される場合があります）。

## 制限事項

本ドライバは機能が非常に限定されています。主な制限事項は以下の通りです。

### SQLクエリの制限 (`CsvStatement#executeQuery`)

*   **SELECT句**: `SELECT *` のみがサポートされています。射影 (例: `SELECT id, name ...`) は動作しません。
*   **FROM句**: SQL構文上は必要ですが、ドライバ内部では無視されます（接続時に指定したCSVファイルが常に使用されます）。
*   **WHERE句**:
    *   単一の `=` (等価比較) のみがサポートされています。
    *   左辺はカラム名、右辺は文字列リテラルである必要があります。
    *   `AND`, `OR`, `LIKE`, 不等号などはサポートされていません。
    *   `WHERE 1 = 1` のような定数式はサポートされていません。
*   **その他**: 全然対応していません。
    
### その他の制限

*   **読み取り専用**: `INSERT`, `UPDATE`, `DELETE` はサポートされていません (`UnsupportedOperationException` がスローされます)。
*   **データ型**: 全てのデータは `String` (VARCHAR) として扱われます。
*   **PreparedStatement**: サポートされていません。
*   **トランザクション**: サポートされていません（常にオートコミット扱い）。
*   **メタデータ**: `jdbc:file://` 接続時のみ簡易的なメタデータ取得が可能ですが、不完全です。
    * 不完全ですが最低限 DBeaver などのSQLクライアントで使えるくらいのメソッドは実装しています
