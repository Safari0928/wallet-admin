import request from '@/utils/request'


export function configureDeposit(params?: any) {
    return request.get({ url: '/depositconfigure/default', params })
}

export function configureDepositUpdate(params?: any) {
    return request.post({ url: '/depositconfigure/update', params })
}






export function configureWithdraw(params?: any) {
    return request.get({ url: '/withdrawconfigure/default', params })
}

export function configureWithdrawUpdate(params?: any) {
    return request.post({ url: '/withdrawconfigure/update', params })
}




export function configureCurrencyList(params?: any) {
    return request.get({ url: '/currencyconfigure/list', params })
}


export function configureCurrency(params?: any) {
    return request.get({ url: '/currencyconfigure/detail', params })
}


export function configureCurrencyUpdate(params?: any) {
    return request.post({ url: '/currencyconfigure/update', params })
}





export function configureTransfer(params?: any) {
    return request.get({ url: '/transferconfigure/default', params })
}

export function configureTransferUpdate(params?: any) {
    return request.post({ url: '/transferconfigure/update', params })
}

