import{C as t}from"./index.28d89b6b.js";function r(){return t.get({url:"/article/category"})}function a(r){return t.get({url:"/article/list",data:r})}function e(r){return t.get({url:"/article/detail",data:r})}function c(r){return t.post({url:"/article/collectAdd",data:r},{isAuth:!0})}function l(r){return t.post({url:"/article/collectCancel",data:r},{isAuth:!0})}function n(){return t.get({url:"/article/collectList"})}export{r as a,e as b,l as c,c as d,n as e,a as g};
