import request from '@/utils/request'


export function transactionList(params?: any) {
    return request.get({ url: '/transactions/list', params })
}

