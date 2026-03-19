# 状态语义统一设计

## 1. 概述

本轮设计聚焦状态标签和短反馈的语义统一，把地图页、岛屿面板、课中页、奖励页以及外围页面里分散的 `TabletChipLabel`、`ParentChipLabel`、`ChestChipLabel`、`AssistChip` 收成一套轻量 `status` 系统。目标不是改变业务状态，也不是一次性重做所有页面，而是在不改稳定合同和交互行为的前提下，让主路径和外围页面里的状态表达读起来属于同一个设计系统。

## 2. 设计目标

- 统一状态标签、提示 pill 和轻反馈的视觉语义。
- 保持现有文案、测试 tag、业务状态和入口行为不变。
- 让地图页、岛屿面板、课中页、奖励页、首页、宝箱页、家长页不再各自维护一套 chip 样式。

## 3. 非目标

- 不改 `ActionButton` 的语义体系。
- 不改地图业务状态、课程流、challenge 规则或奖励逻辑。
- 不新增正式美术资源。

## 4. 状态语义分类

本轮先收 5 类状态语义：

### 4.1 neutral

普通说明标签，例如“探索提示”“线索与问题”。

### 4.2 recommended

推荐推进、当前建议、可继续动作，例如“继续冒险”“已解锁”。

### 4.3 success

完成态、已收入、正向结果，例如“已完成”“本岛完成”“已收入宝箱”。

### 4.4 caution

提醒和限时态，例如“时间到”“冲刺模式”。

### 4.5 highlight

强调反馈和短时提示 pill，例如地图反馈里的星星增长和宝箱提醒。

## 5. 组件策略

### 5.1 StatusTokens

新增 `ui/theme/StatusTokens.kt`，集中定义：

- 每种状态语义的容器色
- 内容色
- 描边色
- 统一圆角和内边距

### 5.2 StatusChip

新增 `ui/components/StatusChip.kt`，作为统一入口：

- 默认渲染 capsule chip
- 支持 leading icon
- 支持保留既有 `modifier` / `testTag`

### 5.3 兼容迁移

`TabletChipLabel` 本轮不直接删除，先收成 `StatusChip(variant = neutral)` 的兼容包装层。`ParentChipLabel`、`ChestChipLabel` 等 feature 私有 helper 则在迁移后删除。

## 6. 首批接入范围

### 6.1 主学习路径

- `feature/map/MapProgressFeedback.kt`
- `feature/map/MapIslandListCard.kt`
- `feature/island/IslandPanelHeader.kt`
- `feature/island/IslandStoryCard.kt`
- `feature/island/IslandLessonCard.kt`
- `feature/level/LevelTabletScreen.kt`
- `feature/level/RewardOverlay.kt`

### 6.2 外围页面

- `feature/home/HomeTabletScreen.kt`
- `feature/chest/ChestTabletScreen.kt`
- `feature/parent/ParentGateScreen.kt`
- `feature/parent/ParentSummaryTabletScreen.kt`

## 7. 稳定合同

本轮不得改动：

- `panel-start-<lessonId>`
- `map-feedback-stars-pill`
- `map-feedback-chest-pill`
- `map-open-chest`
- `map-total-stars`
- 现有状态文案和点击行为

## 8. 验收标准

- 主路径和外围页面不再各自实现私有 chip 样式。
- 状态标签、反馈 pill 和说明标签可以映射到同一套 `status` 语义。
- 现有业务行为、tag 和文案不回归。
- 地图页、岛屿面板、课中页、奖励页、外围页面的状态表达一眼看出属于同一体系。
