# Status Semantics Polish Addendum

## 背景

`surface/card` 和 `action` 两层已经统一，但 `status` 相关表达仍然分散在多个 feature 私有实现里。这会让地图页、岛屿面板、课中页、奖励页和外围页面的状态表达继续分叉。

## 新增目标

- 新增共享 `status` 语义层。
- 收掉主学习路径里的分散 chip / pill 实现。
- 收掉外围页面里的重复 chip helper。

## 并发任务

### 1. status-foundation

- 新增 `StatusTokens`
- 新增 `StatusChip`
- 让 `TabletChipLabel` 退成兼容包装

### 2. main-flow-status-alignment

- 迁移 map / island / level / reward 的状态标签和反馈 pill

### 3. peripheral-status-alignment

- 迁移 home / chest / parent 的状态标签
- 删除 `ChestChipLabel`、`ParentChipLabel`

## 验收

- 主路径与外围页面使用同一套 `status` 语义
- 稳定 tag 不变
- 行为与文案不变
- 统一阶段再跑 `testDebugUnitTest`、`connectedDebugAndroidTest`、`assembleDebug`
