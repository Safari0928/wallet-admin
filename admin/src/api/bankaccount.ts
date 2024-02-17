import config from '@/config'
import request from '@/utils/request'


export function getBankAccount(params?: any) {
    return request.get({ url: '/bankaccount/list' , params })
}
