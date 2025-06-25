create pages components

- List component will help a lot
- Create table component 

- Create brief doc 


val meta = MetaData.of("MyProgramID")

ClientSide.CreateView {
 BaseLayout(title=meta.pageTitle){ 
    row{
        col {
            span=3
            metadata.受注No.let{InputText(it.label){maxlength=it.length}}
        }
        col {
            span=3
            metadata.検索.let {Button(it.label)}
        }
    } 
 }
}.buuild(generateSimpleDoc=Yes)


when generate Simple doc = Yes
generate a html that contains list of component and their attributes