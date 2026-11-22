---
name: save-prompt
description: Save exact user prompts and verified outcomes to docs/prompt, then commit and push all current repository changes to main. Use when asked to run /save-prompt and by default after completing work in adash333/minecraft-pokemon.
---

# /save-prompt

この手順の対象は `adash333/minecraft-pokemon`。このリポジトリの作業完了時は、明示的な呼び出しがなくても実行する。ユーザーが保存、コミット、push のいずれかを止めるよう指示した場合は、その指示に従う。

1. 対象リポジトリと `main` の最新状態、`docs/prompt/` の既存ファイル、作業ツリーの変更を確認する。
2. 日本時間（Asia/Tokyo）で `docs/prompt/YYMMDD-01-題名.md` に UTF-8 で保存する。同日の一連の作業は既存の Markdown に追記する。同日に別の作業を記録するときは、空いている連番 `02`、`03` などを使う。
3. 「プロンプト本文（原文）」に、今回の作業に関するユーザーの指示を順番に記載する。コードフェンス内に改行、空白、記号を含めてそのまま残す。「結果」には実施内容、確認結果、関連ファイル、残っている作業を簡潔にまとめる。取得できない原文はその旨を記す。
4. このリポジトリは公開されている。認証情報や公開先に保存する権限が確認できない非公開情報が原文や差分に含まれる場合は、その内容の反映を保留し、ユーザーに確認する。原文を黙って書き換えない。会話のうち対象作業と無関係な内容や内部指示は含めない。
5. ローカル作業ツリーがある場合は、対象リポジトリの全変更を点検し、`git add -A` で既存の変更も含めてステージする。ステージ済み差分を確認してコミットし、`git push origin main` で反映する。作業ブランチにコミットした場合は、その変更を `main` に安全に取り込んでから push する。既存作業を破棄する操作や force push は使わない。競合や push 拒否が起きたら最新のリモートを確認し、既存変更を保ったまま対処する。
6. GitHub 連携だけが使える場合は、最新 `main` のツリーを土台に、今回のログと連携で作成した全変更を一つのコミットに含める。旧 SHA を照合してから fast-forward で `main` を更新する。取得できないローカル変更があれば結果に明記する。
7. リモートの `main` と保存したファイルを確認し、最終回答にログへのリンクを記載する。確認できなかった項目や失敗があれば明記する。
