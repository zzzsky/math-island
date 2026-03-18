# 数学岛地图动效反馈实施补充计划

> 本文档补充 `2026-03-13-math-island-android-tablet-implementation.md` 与 `2026-03-16-map-island-interaction-addendum.md`，聚焦地图回流时的轻量动效反馈。

## 1. 目标

在现有地图/岛屿交互主路径上补齐三类短反馈：

- 新岛解锁和航线点亮
- 星星增长和顶部统计跳变
- 宝箱按钮短时强调

## 2. 任务拆分

### Task A: 画布动效

**文件：**

- Update: `app/src/main/java/com/mathisland/app/ui/components/IslandMapCanvas.kt`
- Test: `app/src/androidTest/java/com/mathisland/app/ui/components/IslandMapCanvasTest.kt`

**结果：**

- 新岛节点具备短时脉冲
- 航线具备短时高亮

### Task B: 顶部反馈

**文件：**

- Update: `app/src/main/java/com/mathisland/app/feature/map/MapProgressFeedback.kt`
- Test: `app/src/androidTest/java/com/mathisland/app/feature/map/MapProgressFeedbackTest.kt`

**结果：**

- 星星增量有明确强调
- 宝箱状态在反馈条中可见

### Task C: 地图壳层整合

**文件：**

- Update: `app/src/main/java/com/mathisland/app/feature/map/MapTabletScreen.kt`
- Test: `app/src/androidTest/java/com/mathisland/app/feature/map/MapTabletScreenTest.kt`
- Test: `app/src/androidTest/java/com/mathisland/app/MathIslandTabletFlowTest.kt`

**结果：**

- 反馈只消费一次
- 顶部星星和宝箱按钮动效与地图画布同时启动
- 主设备流包含新的回地图反馈断言

## 3. 并行策略

- `Task A` 与 `Task B` 可并发
- `Task C` 负责整合，建议最后落地

## 4. 验收命令

- `./gradlew.bat testDebugUnitTest`
- `./gradlew.bat connectedDebugAndroidTest`
- `./gradlew.bat assembleDebug`
