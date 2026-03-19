# 地图占位美术与课中结算页视觉抛光增量计划

## 关联文档

- 设计说明：
  [2026-03-19-map-art-level-reward-polish-design.md](/D:/Practice/codex/math-island/docs/superpowers/specs/2026-03-19-map-art-level-reward-polish-design.md)
- 基础计划：
  [2026-03-13-math-island-android-tablet-implementation.md](/D:/Practice/codex/math-island/docs/superpowers/plans/2026-03-13-math-island-android-tablet-implementation.md)

## 本轮目标

在保持业务逻辑和稳定合同不变的前提下，同时推进：

1. 地图 placeholder 资源视觉升级
2. 课中页视觉层级抛光
3. 奖励页视觉层级抛光

## 并行任务

### A. map-placeholder-art-polish

- 升级 `map_sea_backdrop`
- 升级 7 座岛的 `island_*_base`
- 升级 7 个 `island_*_icon`
- 升级 `route_segment_default` 和 `route_segment_highlight`
- 必要时微调 map painter，以更好承载新占位资源

### B. level-surface-polish

- 收清 `LevelTabletScreen` 左侧信息区
- 强化进度、流程提示、题目说明的层级
- 保持答题 renderer 合同不变

### C. reward-finish-polish

- 把 `RewardOverlay` 做成更清晰的成绩单结构
- 统一统计卡、结果卡和 CTA 层级
- 不改挑战逻辑和下一步逻辑

## 统一约束

- 不改 slot key
- 不改 `MapArtRegistry` 对外合同
- 不改题目逻辑和 challenge 状态机
- 不改稳定测试 tag

## 阶段验收

收口时统一运行：

- `./gradlew.bat testDebugUnitTest`
- `./gradlew.bat connectedDebugAndroidTest`
- `./gradlew.bat assembleDebug`

阶段结束后按当前协作约定关闭：

- `emulator`
- `qemu`
- `adb`
