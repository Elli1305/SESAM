export enum ColorTheme {'LIGHT', 'DARK'}

export interface Colors {

    id: number,

    defaultColors: boolean,

    theme: ColorTheme,

    logoPath: string,

    bgC: string,
    textC: string,

    primaryColor: string,
    secondary: string,
    accent: string,
    dark: string,
    light: string,
    positive: string,
    negative: string,
    info: string,
    warning: string

}