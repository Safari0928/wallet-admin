import request from '@/utils/request'


export function withdrawList(params?: any) {
    return request.get({ url: '/withdraw/list', params })
}

export function withdrawDetail(params?: any) {
    return request.get({ url: '/withdraw/detail', params })
}

export function withdrawMakeApproved(params?: any) {
    return request.post({ url: '/withdraw/approve', params })
}

export function withdrawCancel(params?: any) {
    return request.post({ url: '/withdraw/cancel', params })
}