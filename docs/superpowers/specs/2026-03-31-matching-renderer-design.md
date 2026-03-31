# 连线配对题型设计

## 目标

在现有 `lesson -> reward -> map` 主链路不变的前提下，新增第一种真正的“新题型”能力：`连线配对`。这一批只落 `MATCHING`，不同时推进 `拖拽填空` 和 `多步操作题`。

目标是让平板端具备一套稳定的拖拽命中与答案编码能力，并通过一节新课或一组新题把它接进现有课程体系。

## 边界

本批次允许变更：

- `Question` 模型的可选扩展字段
- renderer 选择逻辑
- 新增 `MatchingQuestionPane`
- 课程题库映射和一条内容接入
- 对应 unit / androidTest / focused emulator regression

本批次不变更：

- `MathIslandGameController.answer(...)` 接口
- lesson 完成条件
- reward 结算和 map 回流链路
- 现有题型 renderer 行为

## 方案选择

采用“拖拽到目标槽位”的真连线方案，而不是自由画线，也不是伪两步选择。

原因：

- 比“点左点右”的伪连线更像真正新题型
- 比自由画线更稳，命中区、撤销和测试更容易控住
- 后续 `拖拽填空` 可以复用同一套拖拽/命中能力

## 数据模型

在现有 `Question` 上增加两组可选字段：

- `leftItems: List<String> = emptyList()`
- `rightItems: List<String> = emptyList()`

普通题型继续只使用原有 `choices`。  
`MATCHING` 题型使用 `leftItems/rightItems`，并继续把最终答案编码到 `correctChoice`。

## 答案编码

保持 controller 侧的“字符串答案”接口不变。

`MATCHING` 的最终答案采用稳定串编码，按 `leftItems` 顺序拼接：

`leftA=right2,leftB=right1,leftC=right3`

提交规则：

- 所有左侧项都完成配对后才允许提交
- UI 层负责把当前匹配关系编码成上述字符串
- controller 仍只比较 `choice == correctChoice`

## 渲染与交互

新增 `QuestionRendererType.MATCHING` 和 `MatchingQuestionPane`。

交互规则：

- 左列为源项，右列为目标项
- 拖动左项到右侧目标，建立一条连线
- 左项重新拖动时覆盖旧连线
- 已占用的右项被新拖动命中时，替换旧占用
- 已匹配项显示完成态，但在提交前仍可调整

视觉层：

- 中间显示连线层
- 左项卡 / 右项槽位 / 连线共同体现当前匹配状态
- 错误提交走现有 `retry` 语义
- 正确提交走现有 `confirmed` 语义
- 不新增独立 lesson 状态机

## 内容接入

首批内容放在 `classification-island`。

原因：

- 配对天然贴近分类与对应关系
- 不会破坏 `calculation / challenge` 这类已高度稳定的主链路
- 后续更容易扩成 `drag-and-drop` 系列内容

第一批内容建议：

- 新增一节 `classification-match-01`
- 使用无歧义的一对一数学对应关系

## 测试策略

### Unit

- `Question` 的匹配答案编码测试
- `rendererTypeFor` 对 `MATCHING` 的映射测试
- 匹配题状态/覆盖替换规则测试

### AndroidTest

- 新增 `MatchingQuestionPaneTest`
- 在 `LevelAnswerPaneTest` 中补 `renderer-matching`

### Focused Emulator

- 跑新题型 focused regression
- 不要求第一批就把 matching 放进全量主流程用例，但至少要有一条模拟器验收链路

## 文件边界

建议新增或修改：

- 修改：`app/src/main/java/com/mathisland/app/domain/model/GameModels.kt`
- 修改：`app/src/main/java/com/mathisland/app/QuestionRendererType.kt`
- 修改：`app/src/main/java/com/mathisland/app/feature/level/LevelAnswerPane.kt`
- 修改：`app/src/main/java/com/mathisland/app/data/content/CurriculumGameMapping.kt`
- 新增：`app/src/main/java/com/mathisland/app/feature/level/renderers/MatchingQuestionPane.kt`
- 新增：`app/src/main/java/com/mathisland/app/feature/level/renderers/MatchingAnswerState.kt`
- 新增：`app/src/test/java/com/mathisland/app/feature/level/renderers/MatchingAnswerStateTest.kt`
- 新增：`app/src/androidTest/java/com/mathisland/app/feature/level/MatchingQuestionPaneTest.kt`

## 验收标准

- `MATCHING` 可在 lesson 中独立运行
- 题目必须完成全部配对后才能提交
- 提交答案仍走现有 controller 字符串比较链路
- 新题型不会影响现有 6 个 renderer 的行为
- 单测、构建、focused emulator regression 通过
