# 数学岛岛屿详情面板视觉统一补充计划

> 本文档补充 `2026-03-18-map-drawable-resource-chain-addendum.md`，聚焦右侧岛屿详情面板与课程卡的视觉统一。

## 1. 目标

在不改变地图交互和课程入口合同的前提下：

- 用地图已有的 island icon 资源统一岛屿标题区
- 把课程卡收成稳定的视觉组件
- 让 `IslandOverlaySheet` 只保留编排职责

## 2. 任务拆分

### Task A: Header 与 token

**文件：**

- Add: `app/src/main/java/com/mathisland/app/feature/island/IslandPanelTokens.kt`
- Add: `app/src/main/java/com/mathisland/app/feature/island/IslandPanelHeader.kt`

**结果：**

- 岛屿标题区复用 `island_<id>_icon`
- 标题、副标题、进度统一到一套 token

### Task B: 课程卡统一

**文件：**

- Add: `app/src/main/java/com/mathisland/app/feature/island/IslandLessonCard.kt`
- Optionally Add: `app/src/main/java/com/mathisland/app/feature/island/IslandStoryCard.kt`

**结果：**

- 课程卡状态、摘要、CTA 统一
- `panel-start-*` 合同保持不变

### Task C: Overlay 集成与回归

**文件：**

- Update: `app/src/main/java/com/mathisland/app/feature/island/IslandOverlaySheet.kt`
- Update: `app/src/androidTest/java/com/mathisland/app/feature/map/MapTabletScreenTest.kt`
- Update: `app/src/androidTest/java/com/mathisland/app/MathIslandTabletFlowTest.kt`

**结果：**

- overlay 改走新组件
- 焦点切换和课程进入合同不变

## 3. 推荐顺序

1. Header 与 token
2. 课程卡统一
3. Overlay 集成与回归

## 4. 验收命令

- `./gradlew.bat testDebugUnitTest`
- `./gradlew.bat connectedDebugAndroidTest`
- `./gradlew.bat assembleDebug`
