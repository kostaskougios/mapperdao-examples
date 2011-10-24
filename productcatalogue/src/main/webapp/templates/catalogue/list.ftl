[#ftl]
[#include "../layout.ftl"]
[@page]
<h3>The list of products</h3>
<table>
	<tr>
		<th>Title</th>
		<th>Description</th>
		<th>Categories</th>
		<th>Attributes</th>
	</tr>
	[#list products as p]
		<tr>
			<td>${p.title}</td>
			<td>${p.description}</td>
			<td>
				[#list p.categories as c]
					${c.name} [#if c_has_next], [/#if]
				[/#list]
			</td>
			<td>
				[#list p.attributes as a]
					${a.name}: ${a.value} [#if a_has_next], [/#if]
				[/#list]
			</td>
		</tr>
	[/#list]
</table>
[/@page]
