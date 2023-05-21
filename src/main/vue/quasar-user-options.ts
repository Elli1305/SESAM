import "./styles/quasar.scss";
import iconSet from "quasar/icon-set/fontawesome-v5.js";
import "@quasar/extras/fontawesome-v5/fontawesome-v5.css";
// Import QEditor component
import {Dialog, Notify, QEditor} from "quasar";
import langDe from 'quasar/lang/de'

// To be used on app.use(Quasar, { ... })
export default {
    config: {},
    components: {
        // Add QEditor to components
        QEditor,
    },
    plugins: {
        Notify,
        Dialog
    },
    iconSet: iconSet,
    framework: {
        lang: 'de'
    },
    lang: langDe
};
