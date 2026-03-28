# 地图回流结果舞台设计

## 目标

把奖励页、地图回流摘要、岛屿 handoff 三处结果反馈收成同一套“结果舞台”，让用户在 `结算 -> 回地图 -> 继续下一步` 这一段里看到一致的标题、标签和下一步提示。

## 设计

- 共享 `MapReturnCopy` 扩展为两层信息：
  - `summary`：给奖励页和地图回流摘要用的主结论
  - `detail`：给“回地图后会发生什么”用的下一步提示
- `RewardOverlay` 继续保留结果页主 spotlight，但在“继续航线”区增加 detail card。
- `MapReturnSummaryCard` 不再只有一张 spotlight card，而是追加同一套 detail card。
- `IslandHandoffCard` 与地图回流摘要对齐，使用同一套 summary + detail 结构。

## 范围

- 包含：`RewardViewModel`、`MapFeedbackMapper`、`MapProgressFeedback/MapReturnSummaryCard`、`IslandViewModel/IslandHandoffCard`
- 不包含：新的业务状态、地图动效、课程/奖励逻辑变更

## 验收

- 单测覆盖共享 copy 和 viewmodel 映射
- focused emulator 回归覆盖奖励页、岛屿 handoff、地图回流摘要
- 全量 `testDebugUnitTest` 和 `assembleDebug` 继续通过
