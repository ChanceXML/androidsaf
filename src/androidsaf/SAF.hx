package androidsaf;

#if android
import lime.system.JNI;
#end

class SAF
{
    #if android
    static var openPicker:Dynamic;

    static function init()
    {
        if (openPicker == null)
        {
            openPicker = JNI.createStaticMethod(
                "com/shadowmario/psychengine/SAFBridge",
                "openModsFolderPicker",
                "()V"
            );
        }
    }

    public static function openModsFolder():Void
    {
        init();

        if (openPicker != null)
            openPicker();
    }
    #else
    public static function openModsFolder():Void
    {
        trace("Android only");
    }
    #end
}
