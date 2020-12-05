import axios from 'axios'
/**
 * get请求
 * @param {*} url 
 * @param {*} paramsObj 
 */
export function getJSON(url, paramsObj) {
    return new Promise((resolve, reject) => {
        axios.get(url, {
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded'
            },
            params: paramsObj
        }).then((res) => {
            resolve(res)
        }).catch((e) => {
            reject(e)
        })
    })
}
/**
 * post传输json对象
 * @param {*} url 
 * @param {*} paramsObj 
 */
export function postJSON(url, paramsObj) {
    return new Promise((resolve, reject) => {
        axios.post(url, 
            paramsObj
        ).then((res) => {
            resolve(res)
        }).catch((e) => {
            reject(e)
        })
    })
}
export function postUrl(url, paramsObj) {
    return new Promise((resolve, reject) => {
        axios({
            method: 'post',
            url: url,
            params: paramsObj,
            responseType: 'blob',
          }).then((res) => {
            resolve(res)
        }).catch((e) => {
            reject(e)
        })
    })
}

export function postFile(url, data) {
    return new Promise((resolve, reject) => {
        axios.post(url,data, {
            headers: {
                'Content-Type': 'multipart/form-data',
            },
        }).then((res) => {
            resolve(res)
        }).catch((e) => {
            reject(e)
        })
    })
}

export function copy(obj) {
    let newobj = obj.constructor === Array ? [] : {};
    if(typeof obj !== 'object'){
        return;
    }
    for(let i in obj){
        newobj[i] = typeof obj[i] === 'object' ? copy(obj[i]) : obj[i];
    }
    return newobj
}