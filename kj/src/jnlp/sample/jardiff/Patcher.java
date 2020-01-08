package jnlp.sample.jardiff;

import java.io.IOException;
import java.io.OutputStream;

public abstract interface Patcher
{
  public abstract void applyPatch(PatchDelegate paramPatchDelegate, String paramString1, String paramString2, OutputStream paramOutputStream)
    throws IOException;
  
  public static abstract interface PatchDelegate
  {
    public abstract void patching(int paramInt);
  }
}