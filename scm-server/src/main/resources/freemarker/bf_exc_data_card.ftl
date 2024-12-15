{
"config": {
"update_multi": true
},
"i18n_elements": {
"zh_cn": [
{
"tag": "column_set",
"flex_mode": "none",
"horizontal_spacing": "8px",
"horizontal_align": "left",
"columns": [
{
"tag": "column",
"width": "weighted",
"vertical_align": "top",
"vertical_spacing": "8px",
"elements": [
{
"tag": "markdown",
"content": "${content!''}",
"text_align": "left"
}
],
"weight": 1
},
{
"tag": "column",
"width": "auto",
"vertical_align": "top",
"vertical_spacing": "8px",
"elements": [
{
"tag": "img",
"img_key": "${imgCode!''}",
"preview": true,
"scale_type": "crop_center",
"size": "medium",
"alt": {
"tag": "plain_text",
"content": ""
}
}
]
}
],
"margin": "16px 0px 0px 0px"
}
]
},
"i18n_header": {
"zh_cn": {
"title": {
"tag": "plain_text",
"content": "${title!''}"
},
"template": "purple"
}
}
}
