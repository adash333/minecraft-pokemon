# 261122 GitHub接続確認と /save-prompt の設定

- 対象リポジトリは `adash333/minecraft-pokemon`。
- 日付の基準は日本時間（Asia/Tokyo）。

## プロンプト本文（原文）

### GitHub接続確認

```text
@github https://github.com/adash333/minecraft-pokemon　これに接続できますか？
```

### スキルの保存と実行

```text
スキル　/save-prompt として、以下の内容を保存して、実行してください。また、基本的に、毎回、自動で/save-promptを実行してほしいです
docs/prompt/ フォルダに、上の　「プロンプト本文」（そのまま）　と　その結果の内容をまとめたものを　YYMMDD-(複数あれば連番 01 など)-題名.md という名前で保存します。同日のmdファイルと一連の流れでまとめられる内容は、一つのmdファイルにまとめるようにしてください。さらに、 それまでのすべての変更をgit addしてmainにpushすることを、　スキル　/save-prompt として保存して下さい。　このスキルはChatGPTとClaude両方が行えるようにして下さい
```

## 結果

- GitHub 連携で公開リポジトリにアクセスし、デフォルトブランチが `main` で、読み取りと書き込みの権限があることを確認した。
- [共通スキル](../../.agents/skills/save-prompt/SKILL.md) と [Claude Code 用スキル](../../.claude/skills/save-prompt/SKILL.md) を追加した。[AGENTS.md](../../AGENTS.md) と [CLAUDE.md](../../CLAUDE.md) で、明示的に停止を指示された場合を除き、対象リポジトリの作業完了時に既定で実行する。
- 同日に作成されたログがなかったため連番は `01` とした。接続確認と設定の記録をこのファイルにまとめた。
- ローカルの Git 作業ツリーがなかったため、GitHub 連携のコミットに今回の全変更をまとめた。Claude Code での実行は未検証。
