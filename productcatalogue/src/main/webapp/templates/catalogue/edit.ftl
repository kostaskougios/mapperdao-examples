[#ftl]
[#include "../layout.ftl"]
[@page]
<h3>Edit Product</h3>
<form method="post">
	<label>Title</label>
	<input name="title" value="${product.title}"/>
	<br/>
	<label>Description</label>
	<textarea rows="10" cols="80" name="description">${product.description}</textarea>
</form>
[/@page]