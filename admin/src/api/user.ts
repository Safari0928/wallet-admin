import config from '@/config'
import request from '@/utils/request'


export function login(params: Record<string, any>) {
    return request.post({ url: '/system/login', params: { ...params, terminal: config.terminal } })
}


export function logout() {
    return request.post({ url: '/system/logout' })
}


export function getUserInfo() {
    return request.get({ url: '/system/admin/self' })
}


export function getMenu() {
    return request.get({ url: '/system/menu/route' })
}


export function setUserInfo(params: any) {
    return request.post({ url: '/system/admin/change', params })
}

export function userList(params?: any) {
    return request.get({ url: '/user/list', params })
}


export function userDetail(params?: any) {
    return request.get({ url: '/user/detail', params })
}

export function getUserCountries(params?: any) {
    return request.get({ url: '/user/country', params })
}

export function setUser(params?: any) {
    return request.post({ url: '/user/update', params })
}



