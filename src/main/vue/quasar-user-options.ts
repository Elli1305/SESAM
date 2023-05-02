import "./styles/quasar.scss";
import iconSet from "quasar/icon-set/fontawesome-v5.js";
import "@quasar/extras/fontawesome-v5/fontawesome-v5.css";
import { Notify } from "quasar";
import langDe from 'quasar/lang/de'

// Import QEditor component
import { QEditor } from "quasar";

// To be used on app.use(Quasar, { ... })
export default {
    config: {},
    components: {
        // Add QEditor to components
        QEditor,
    },
    plugins: {
        Notify,
    },
    iconSet: iconSet,
    framework: {
        lang: 'de'
    },
    lang: langDe
};
