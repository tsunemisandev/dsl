# メタデータ駆動型UIコード生成プロジェクト

このプロジェクトは、Kotlin で一元管理されたフィールドメタデータ（`FieldMetadata`）をもとに、  
Vue3 + Nuxt 用のフロントエンドコンポーネントや型定義（TypeScript）を**自動生成**します。  
これにより、業務画面（OrderForm, CustomerList等）のUI仕様を一箇所で管理でき、  
変更時のメンテナンス工数を大幅に削減できます。

---

## 主な特徴

- **メタデータ一元管理**
    - フィールド名、ラベル、最大長、選択肢等をKotlinで定義
    - 自動でTypeScript用 `MetaDataField.ts` を出力

- **自動コンポーネント生成**
    - 入力フォームやテーブル等のVueコンポーネントをKotlin DSLで定義
    - 生成されたSFCは`MetaDataField.ts`を参照し、常に最新の仕様と同期

- **型安全 & 保守性**
    - TypeScript型定義も自動生成
    - UI仕様変更はメタデータ修正のみでOK

---
/metadata/MetaDataField.ts # TypeScriptで出力されるメタデータ
/pages/OrderForm/components/ # 自動生成されたVueコンポーネント
/pages/CustomerList/components/ # 同上
/pages/OrderForm/types/ # 自動生成されたTypeScript型
/pages/OrderForm/docs/ # ドキュメント（HTML出力）
/generator/ # コード生成ロジック（Kotlin）
/template/ # テンプレート実装（Kotlin）

yaml
コピーする
編集する

---

## 使い方

1. **Kotlinの`FieldMetadata`を編集**
2. `generator`を実行し、コンポーネント/型/メタデータTSを自動生成
3. Nuxt/Vue3側でSFCを利用（`MetaDataField.ts`経由で動的仕様反映）

---

## 利点

- **画面仕様変更がメタデータ一括修正のみで完結**
- **コード重複やヒューマンエラー削減**
- **現場/非エンジニアもメタデータ編集だけでフィールド追加・仕様調整が可能**

---

## 参考

- 画面フィールド仕様の追加・修正は `/metadata/MetaDataField.ts` へ
- 新規画面追加時も同様の手順でKotlin DSL/メタデータ→自動生成OK

---