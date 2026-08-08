# Minecraft Pokemon Gacha

Minecraft Java Edition 1.20.1 / Forge 用の小さな Mod です。

## 入っているもの

- ワールド初回起動時、スポーン地点近くに 2 つのガチャを自動生成
  - モンスターボールガチャ
  - もものみガチャ
- ガチャを右クリックするとアイテムを入手
- ミュウ風の「ミュウブロック」がスポーン地点近くに出現
- モンスターボールを投げて当てると捕獲判定
  - 70%: 捕獲成功。プレイヤーになつく
  - 30%: 逃げる
- なついたミュウブロックは、10 ブロックより離れるとプレイヤーへ近づく
- ミュウブロックが100匹以上になると、なついていない個体だけが消える
- もものみをあげるとハートを出して喜ぶ
- もものみをあげたとき、25% の確率で少し大きく進化
- ミニリュウがプレイヤーの近くに15秒ごとに2匹出現
- 食べ物ガチャから「虹色のマンゴー」が50%の確率で出現
- ミニリュウに虹色のマンゴーをあげると、ハクリュウ、カイリューの順に1段階ずつ進化
- ミニリュウ、ハクリュウ、カイリューはモンスターボールで70%の確率でなつく
- なついた個体は飼い主を追いかけ、カイリューは翼で空を飛んでついてくる
- ミニリュウ、ハクリュウ、カイリューが合計100匹以上になると、なついていない個体だけが消える

## ビルド方法

このフォルダで Gradle を使ってビルドします。

```powershell
gradle build
```

ビルドできたら、次の jar を CurseForge の `mods` フォルダへ入れます。

```text
build/libs/minecraft-pokemon-<VERSION>.jar
```

## バージョン管理

- 現在のバージョンはルートの `VERSION` ファイルで管理します。
- 確認用の `gradle build` ではバージョンは変わりません。
- 配布版は `gradle release` で作成します。成功するとパッチ番号が1つ上がり、その番号の jar が作成されます。
- バージョンは `1.0.6`、`1.0.7`、`1.0.8` の順で増やします。

## CurseForge で遊ぶ手順

1. CurseForge で Minecraft Java Edition 1.20.1 の Forge プロファイルを作る
2. この Mod をビルドする
3. 出来上がった jar をプロファイルの `mods` フォルダへ入れる
4. 新しいワールドを作る
5. スポーン地点近くにガチャとミュウブロックが出る

## メモ

今の見た目は Mod 内蔵の簡易テクスチャです。あとでより本物っぽいリソースパックに差し替える場合は、以下を変更します。

- `src/main/resources/assets/minecraftpokemon/textures/entity/mew_block.png`
- `src/main/resources/assets/minecraftpokemon/textures/item/monster_ball.png`
- `src/main/resources/assets/minecraftpokemon/textures/item/peach_berry.png`
- `src/main/resources/assets/minecraftpokemon/textures/block/ball_gacha.png`
- `src/main/resources/assets/minecraftpokemon/textures/block/food_gacha.png`
#
