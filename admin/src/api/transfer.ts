import request from '@/utils/request'

// 文章分类列表
export function transferList(params?: any) {
    return request.get({ url: '/transfer/list', params })
}
// 文章分类列表
export function transferDetail(params?: any) {
    return request.get({ url: '/transfer/detail', params })
}
