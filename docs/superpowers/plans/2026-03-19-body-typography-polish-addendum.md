# Body Typography Polish Addendum

## 背景

标题层级和 spacing/radius 已经收过，但正文和辅助说明还分散在多个 feature 与组件里，尤其是 `bodyLarge/bodyMedium/titleMedium` 与一批不同透明度的 `onSurface.copy(alpha = ...)`。

## 新增目标

- 建立 shared body typography 语义
- 建立 shared text tone 语义
- 先统一卡片说明文，再统一页面辅助提示

## 并发任务

### 1. body-foundation

- 扩展 `TypographyTokens`
- 新增 `TextToneTokens`

### 2. card-copy-alignment

- 迁移 `TabletInfoCard / TabletActionCard / IslandStoryCard / RewardOverlay`

### 3. page-supporting-copy-alignment

- 迁移 `HomeTabletScreen / LevelTabletScreen / ChestTabletScreen / MapProgressFeedback`

## 验收

- 卡片说明文与页面辅助说明形成统一层级
- 行为与稳定合同不变
- 阶段收口时统一跑 `testDebugUnitTest`、`assembleDebug`
