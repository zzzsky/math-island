# 外围页面按钮语义扩展与地图动效抛光增量计划

## 关联文档

- 设计说明：
  [2026-03-19-peripheral-actions-and-map-motion-design.md](/D:/Practice/codex/math-island/docs/superpowers/specs/2026-03-19-peripheral-actions-and-map-motion-design.md)

## 本轮目标

并行推进：

1. `Home / Chest / Parent` 接入共享 action 语义
2. 地图反馈从轻强调推进到更完整的节点/航线动效

## 并行任务

### A. peripheral-action-alignment

- `HomeTabletScreen`
- `ChestTabletScreen`
- `ParentGateScreen`
- `ParentSummaryTabletScreen`
- 必要时 `TabletActionCard`

### B. map-motion-polish

- `MapTabletScreen`
- `IslandMapCanvas`
- `MapProgressFeedback`
- 必要时 `ui/components/map/*`

## 合入顺序

1. `peripheral-action-alignment`
2. `map-motion-polish`

## 阶段验收

收口时统一运行：

- `./gradlew.bat testDebugUnitTest`
- `./gradlew.bat connectedDebugAndroidTest`
- `./gradlew.bat assembleDebug`

阶段结束后关闭：

- `emulator`
- `qemu`
- `adb`
