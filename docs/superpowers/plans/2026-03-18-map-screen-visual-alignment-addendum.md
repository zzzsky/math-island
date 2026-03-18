# 数学岛地图页视觉统一补充计划

> 本文档补充 `2026-03-18-island-panel-visual-alignment-addendum.md`，聚焦地图页内部的统一视觉收口。

## 1. 目标

在不改变地图行为的前提下：

- 统一左侧岛屿列表卡与右侧面板视觉
- 统一顶部工具条风格
- 缩减 `MapTabletScreen` 内联样式职责

## 2. 任务拆分

### Task A: 岛屿列表卡统一

**文件：**

- Add: `app/src/main/java/com/mathisland/app/feature/map/MapIslandListCard.kt`
- Update: `app/src/main/java/com/mathisland/app/feature/map/MapTabletScreen.kt`

**结果：**

- 左侧岛屿卡复用 island icon 与同源 token
- `select-island-*` 合同保持不变

### Task B: Top bar 统一

**文件：**

- Add: `app/src/main/java/com/mathisland/app/feature/map/MapTopBar.kt`
- Optionally Add: `app/src/main/java/com/mathisland/app/feature/map/MapScreenTokens.kt`
- Update: `app/src/main/java/com/mathisland/app/feature/map/MapTabletScreen.kt`

**结果：**

- 返回首页、打开宝箱、总星星 pill 统一风格
- `map-open-chest` / `map-total-stars*` 合同保持不变

### Task C: 集成与回归

**文件：**

- Update: `app/src/androidTest/java/com/mathisland/app/feature/map/MapTabletScreenTest.kt`
- Update: `app/src/androidTest/java/com/mathisland/app/MathIslandTabletFlowTest.kt`

**结果：**

- 地图页主流程不回归
- 左侧卡片与右侧面板联动仍稳定

## 3. 推荐顺序

1. 岛屿列表卡统一
2. Top bar 统一
3. 集成与回归

## 4. 验收命令

- `./gradlew.bat testDebugUnitTest`
- `./gradlew.bat connectedDebugAndroidTest`
- `./gradlew.bat assembleDebug`
