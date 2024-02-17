import config from '@/config'
import request from '@/utils/request'


export function getBankCard(params?: any) {
    return request.get({ url: '/bankcard/list' , params })
}
