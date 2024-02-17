import request from '@/utils/request'

export function handlingFeeList(params?: any) {
    return request.get({ url: '/handlingfee/list', params })
}
