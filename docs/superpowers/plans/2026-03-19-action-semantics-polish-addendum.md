# 主路径按钮语义统一增量计划

## 关联文档

- 设计说明：
  [2026-03-19-action-semantics-polish-design.md](/D:/Practice/codex/math-island/docs/superpowers/specs/2026-03-19-action-semantics-polish-design.md)

## 本轮目标

建立轻量 action 语义层，并优先接入主学习路径：

1. action token 和共享按钮入口
2. 地图与岛屿面板按钮接入
3. 课中页与奖励页按钮接入

## 并行任务

### A. action-foundation

- 新增 `ActionTokens`
- 新增 `ActionButton`
- 把 `WoodButton` 收成兼容包装层

### B. map-island-action-alignment

- `MapTopBar`
- `IslandOverlaySheet`
- `IslandLessonCard`

### C. level-reward-action-alignment

- `LevelTabletScreen`
- `RewardOverlay`

## 统一约束

- 不改按钮文案
- 不改测试 tag
- 不改业务逻辑
- 不把外围页面一起卷入

## 阶段验收

收口时统一运行：

- `./gradlew.bat testDebugUnitTest`
- `./gradlew.bat connectedDebugAndroidTest`
- `./gradlew.bat assembleDebug`

阶段结束后关闭：

- `emulator`
- `qemu`
- `adb`
