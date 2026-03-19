# 主路径按钮语义统一设计

## 1. 概述

本轮设计聚焦主路径按钮系统，把地图页、岛屿面板、课中页和奖励页中分散的按钮语义收成一套轻量 action 体系。目标不是重做交互，也不是全项目一次性统一所有按钮，而是在不改变行为和稳定测试合同的前提下，让主学习路径的操作按钮不再依赖各 feature 自己写死的颜色和样式。

## 2. 设计目标

- 把主路径按钮统一到少量明确语义，而不是继续散落在 `WoodButton`、`ButtonDefaults` 和各 feature 私有颜色里。
- 保持现有交互、按钮文案、测试 tag 和业务状态不变。
- 让地图、岛屿面板、课中页和奖励页的按钮一眼看出属于同一套系统。

## 3. 非目标

- 不在这轮统一 `HomeTabletScreen`、`ChestTabletScreen`、`ParentGateScreen`、`ParentSummaryTabletScreen` 的按钮。
- 不改 renderer 内部答题按钮。
- 不改业务逻辑、challenge 流、返回路径和 CTA 文案。

## 4. 语义分类

本轮 action 语义限定为 5 类：

### 4.1 primary

用于主继续动作，例如奖励页主 CTA。

### 4.2 secondary

用于普通返回或次级导航，例如课中页“返回地图”。

### 4.3 recommended

用于推荐推进动作，例如地图页“打开宝箱”、岛屿面板主推荐课程、课程卡里的推荐开始。

### 4.4 completed

用于完成态后的再次练习动作，例如“再次练习”。

### 4.5 outlined-secondary

用于并列但明显次级的操作，例如奖励页“再试冲刺”。

## 5. 组件策略

### 5.1 ActionTokens

新增 `ui/theme/ActionTokens.kt`，负责：

- 各 action 语义的容器色
- 内容色
- 描边色
- 统一圆角和默认高度/内边距

### 5.2 ActionButton

新增 `ui/components/ActionButton.kt`，负责：

- 根据语义应用默认 `Button` 或 `OutlinedButton` 样式
- 暴露稳定的 `modifier` 和 `enabled` 行为
- 保持调用点只声明语义，不再自己拼 `ButtonDefaults`

### 5.3 WoodButton 的处理

`WoodButton` 本轮不删除，先收成兼容包装层：

- 内部转发到 `ActionButton`
- 让当前主路径先切过去
- 其余页面后续再迁移

## 6. 首批接入文件

### 6.1 地图与岛屿面板

- `feature/map/MapTopBar.kt`
  - 返回首页 -> `secondary`
  - 打开宝箱 -> `recommended`
- `feature/island/IslandOverlaySheet.kt`
  - 主岛屿 CTA -> `recommended / completed`
- `feature/island/IslandLessonCard.kt`
  - 课程 CTA -> `recommended / completed / secondary`

### 6.2 课中与奖励

- `feature/level/LevelTabletScreen.kt`
  - 返回地图 -> `secondary`
- `feature/level/RewardOverlay.kt`
  - 主继续按钮 -> `primary`
  - 再试冲刺 -> `outlined-secondary`

## 7. 稳定合同

本轮不得改动：

- `map-open-chest`
- `panel-start-<lessonId>`
- `reward-retry-sprint`
- `reward-return-map`
- 现有按钮文案和点击行为

## 8. 验收标准

- 主路径按钮不再主要依赖散落的 `ButtonDefaults.buttonColors(...)`
- 5 类 action 语义可以覆盖当前主路径按钮
- 行为、文案、测试 tag 和业务状态完全不变
- 地图、岛屿面板、课中页、奖励页的按钮风格读起来属于同一个系统
