import request from '@/utils/request'

// 配置
export function getConfig() {
    return request.get({ url: '/index/config' })
}

// 工作台主页
export function getWorkbench() {
    return request.get({ url: '/index/console' })
}

// graph
export function getGraph(params?: any) {
    return request.get({ url: '/dashboard/graph' , params})
}

// chart
export function getChart(params?: any) {
    return request.get({ url: '/dashboard/chart' , params})
}

// panel
export function getPanel(params?: any) {
    return request.get({ url: '/dashboard/panel' , params})
}

// tank
export function getRank(params?: any) {
    return request.get({ url: '/dashboard/rank' , params})
}


