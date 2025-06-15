package model

interface LayoutPart

abstract class UIComponent : LayoutPart

class InputText(val meta: FieldMeta) : UIComponent()
class Button(val meta: FieldMeta) : UIComponent()
class Table(val meta: TableMeta) : LayoutPart
class SubLayout(val layout: dsl.Layout) : LayoutPart

class Row(val cols: List<Col>) : LayoutPart
class Col(val span: Int = 1, val parts: List<LayoutPart>) : LayoutPart
class Tab(val panes: List<TabPane>) : LayoutPart
class TabPane(val label: String, val parts: List<LayoutPart>) : LayoutPart

