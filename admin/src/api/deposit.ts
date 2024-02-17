import request from '@/utils/request'

// 文章分类列表
export function depositList(params?: any) {
    return request.get({ url: '/deposit/list', params })
}
// 文章分类列表
export function depositDetail(params?: any) {
    return request.get({ url: '/deposit/detail', params })
}
