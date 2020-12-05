/* eslint-disable no-mixed-spaces-and-tabs */
const webpack = require('webpack')
const port = 40000;
const host = 'localhost'
const remoteIp = 'http://localhost:8080' // 前端静态资源代理地址

module.exports = {
    // 打包输入文件夹路径
	devServer: {
		// 设置主机地址
		host: host,
		// 设置默认端口
		port: port,
		// 设置代理
		proxy: {
			'/*': {
				// 目标 API 地址
				target: remoteIp,
				// 如果要代理 websockets
				ws: false
			}
        },
        overlay: {
            warnings: false,
            errors: false
		},
		historyApiFallback: true,
    },
    lintOnSave: false,
    configureWebpack: (config) => {
        // 全局引入jquery
        config.plugins.push(
        	new webpack.ProvidePlugin({
        		$: 'jquery',
        	})
        )
	},
	chainWebpack: (config) => {       
        config.module.rule('images')
              .test(/\.(png|jpe?g|jpg|gif|svg)(\?.*)?$/)
			  .use('url-loader')
			  .loader('url-loader')
			  .tap(options => Object.assign(options, { limit: 2000, esModule: false }));
			  //file-loader默认采用ES模块语法，即import ‘…/image.png’；然而Vue生成的是CommonJS模块语法，即require(’…/image.png’)；二者不一致
    }
}
