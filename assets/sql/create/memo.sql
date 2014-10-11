

/* Create Tables */

CREATE TABLE MEMO
(
	-- 主キー
	ID INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
	-- ジャンルを保持
	GENRE TEXT,
	-- メモ内容を保持
	MEMO TEXT NOT NULL,
	-- 作成日時（unixtime）
	CREATETIME INTEGER NOT NULL,
	-- 更新日時（unixtime）
	UPDATETIME INTEGER NOT NULL
);



